chestInventory = function(canvasId, scale, name)
{
	this.getSlotCoordinates = function(slot)
	{
		return {x: (8 + (slot % 9) * 18) * this.scale, y: (18 + Math.floor(slot / 9) * 18) * this.scale};
	};
	
	this.getStackAtCoordinates = function(x, y)
	{
		//Loop through all the filled slots
		for (i in this.content)
		{
			//Make a variable for the start coordinates of the current slot (shorter in the if)
			var slotCoordinates = this.getSlotCoordinates(i);
			/*Check if the Mouse is over the slot
			 >= to start coordinates
			 <= to start coordinates + the scaled size of the slot */
			if (x >= slotCoordinates.x && 
				y >= slotCoordinates.y &&
				x <= slotCoordinates.x + 16 * this.scale &&
				y <= slotCoordinates.y + 16 * this.scale)
			{
				//Mouse is over a filled slot
				return this.content[i].stack;
			}
		}
		//If the Mouse is not over a filled slot, return false
		return false;
	};
	
	this.drawContent = function()
	{
		//Draw Inventory background (to scale)
		//Top
		if (this.inventoryImages.top.complete)
		{
			this.context.drawImage(this.inventoryImages.top, 0, 0, 176 * this.scale, 17 * this.scale);
		}
		//Rows
		if (this.inventoryImages.middle.complete)
		{
			for (i = 0; i < this.rows; i++)
			{
				this.context.drawImage(this.inventoryImages.middle, 0, (17 + 18 * i) * this.scale, 176 * this.scale, 18 * this.scale);
			}
		}
		//Bottom
		if (this.inventoryImages.bottom.complete)
		{
			this.context.drawImage(this.inventoryImages.bottom, 0, (17 + 18 * this.rows) * this.scale, 176 * this.scale, 7 * this.scale);
		}
		//Loop through all the filled slots and draw their image at the slot's coordinates; to scale
		for (i in this.content)
		{
			if (this.content[i].complete && this.content[i].animation == null)
			{
				var coords = this.getSlotCoordinates(i);
				this.context.drawImage(this.content[i], coords.x, coords.y, 16 * this.scale, 16 * this.scale);
			}
		}
	};
	
	this.drawAnimatedContent = function(ignore)
	{
		for (i in this.animatedContent)
		{
			var content = this.content[this.animatedContent[i]];
			if (this.content.complete)
			{
				if (!ignore)
				{
					//Increment the duration of the current frame
					content.animation.currentFrameDuration++;
					//If the duration is (higher or) equal to the duration it should stay, go draw the next frame and go to the next one
					if (content.animation.currentFrameDuration >= content.animation.frames[content.animation.currentFrame].duration)
					{
						//Increment the currentFrame or revert it to the first when at the end
						content.animation.currentFrame = (content.animation.currentFrame + 1) % content.animation.frames.length;
						content.animation.currentFrameDuration = 0;
					}
				}
				//draw the new frame to the canvas
				//drawImage(image, src_start_x, src_start_y, src_size_x, src_size_y, dest_start_x, dest_start_y, dest_size_x, dest_size_y);
				var coords = this.getSlotCoordinates(this.animatedContent[i]);
				this.context.drawImage(content, 0, 16 + 16 * content.animation.currentFrame, 16, 16, coords.x, coords.y, 16 * this.scale, 16 * this.scale);
			}
		}
	};
	
	this.drawName = function()
	{
		var str = this.name;
		if (this.position.x != null && this.position.y != null && this.position.z != null)
		{
			str += " at " + this.position.x + ", " + this.position.y + ", " + this.position.z;
		}
		this.context.font = (8 * this.scale) + "px Arial";
		this.context.textAlign = "center";
		this.context.textBaseline = "middle";
		this.context.fillText(str, 88 * this.scale, 10 * this.scale, 164 * this.scale);
	};
	
	this.setScale = function(nScale)
	{
		if (parseFloat(nScale).toString() != "NaN")
		{
			nScale = parseFloat(nScale);
		}
		//If the new scale is different from the old, change it
		if (nScale != this.scale && nScale > 0)
		{
			this.scale = nScale;
			//Change the canvas dimensions to fit
			this.canvas.width = 176 * nScale;
			this.canvas.height = (24 + 18 * this.rows) * nScale;
			//Call the update function so it gets redrawn
			this.update();
		}
	};
	
	this.setContent = function(nContent)
	{
		//new empty arrays for the new content(s)
		var nContentArray = [];
		var nAnimatedContentArray = [];
		//Loop through the stack-objects in the received array
		for (i in nContent)
		{
			var image = new Image();
				image.parent = this;
				image.onload = function() { this.parent.update(); };
				image.src = "http://sp.svennp.com/invscan/texture/" + nContent[i].rawName + ".png";
			if (nContent[i].animation != null && nContent[i].animation != "")
			{
				image.animation = parseAnimation(nContent[i].animation);
				nAnimatedContentArray[nAnimatedContentArray.length] = nContentArray.length;
			}
			nContentArray[nContentArray.length] = image;
		}
		this.content = nContentArray;
		this.animatedContent = nAnimatedContentArray;
		var rows = Math.ceil(this.content.length / 9);
		this.rows = rows > 0 ? rows : 1;
		this.canvas.height = (24 + 18 * this.rows) * this.scale;
	};
	
	this.setChestName = function(name)
	{
		//If the new name is different from the old, change it
		if (name != this.name)
		{
			//Cache current inventory if it's not yet
			if (this.cachedInventories[this.name] == null)
			{
				this.cachedInventories[this.name] = {content: this.content, animatedContent: this.animatedContent};
			}
			
			//Get the inventory for the chest
			if (this.cachedInventories[name] == null)
			{
				//Request from server if it's not in the cache
				this.getInventoryFor(name);
			}
			else
			{
				//Load it from the cache
				this.content = this.cachedInventories[name].content;
				this.animatedContent = this.cachedInventories[name].animatedContent;
			}
			//Set the player name
			this.name = name;
			//Call the update function
			this.update();
		}
	};
	
	this.getInventoryFor = function(name)
	{
		var xmlhttp = new XMLHttpRequest();
			xmlhttp.parent = this;
			//Set the function that calls the setContent method if it successfully completes
			xmlhttp.onreadystatechange = function()
			{
				//Check if the request successfully completed
				if (this.readyState == this.DONE && this.status == 200)
				{
					//Parse the JSON returned by the getInventory.php into a object
					var content = JSON.parse(this.responseText);
					//Check that it's not null
					if (content != null)
					{
						//Update the inventory's content with the newly received
						this.parent.setContent(content);
					}
				}
			};
			//Set the request url to the getInventory.php
			xmlhttp.open("GET", "http://sp.svennp.com/invscan/php/getChestInventory.php?name=" + name + "&time=" + new Date().getTime());
			//Send the request
			xmlhttp.send();
	};
	
	this.update = function()
	{
		this.drawContent();
		this.drawAnimatedContent(true);
		this.drawName();
	};
	
	this.cachedInventories = {};
	this.name = name != null ? name : "Chest";
	this.scale = parseFloat(scale).toString() != "NaN" ? parseFloat(scale) : 1;
	this.position = {x: null, y: null, z: null};
	this.content = [];
	this.animatedContent = [];
	this.rows = 1;
	this.canvas = document.getElementById(canvasId);
		this.canvas.width = 176 * this.scale;
		this.canvas.height = (24 + 18 * this.rows) * this.scale;
	this.context = this.canvas.getContext("2d");
	this.inventoryImages = {};
	var imgs = ["top", "middle", "bottom"];
	for (i in imgs)
	{
		this.inventoryImages[imgs[i]] = new Image();
			this.inventoryImages[imgs[i]].parent = this;
			this.inventoryImages[imgs[i]].onload = function() { this.parent.update(); };
			this.inventoryImages[imgs[i]].src = "./texture/chestInventory-" + imgs[i] + ".png";
	}
}