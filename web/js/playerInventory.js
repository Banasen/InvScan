playerInventory = function(canvasId, scale)
{	
	this.getSlotCoordinates = function(s)
	{
		var coordinates = [];
		//Hotbar
		for (a = 0; a < 9; a++)
		{
			coordinates[coordinates.length] = {x: (8 + (18 * a)) * s, y: 142 * s};
		}
		//Main Inventory
		for (a = 0; a < 3; a++)	
		{
			for (b = 0; b < 9; b++)
			{
				coordinates[coordinates.length] = {x: (8 + (18 * b)) * s, y: (84 + (18 * a)) * s};
			}
		}
		//Armor
		for (a = 3; a >= 0; a--)
		{
			coordinates[coordinates.length] = {x: 8 * s, y: (8 + (18 * a)) * s};
		}
		return coordinates;
	};
	
	this.getStackAtCoordinates = function(x, y)
	{
		//Loop through all the filled slots
		cntnt = ["content", "animatedContent"];
		for (c in cntnt)
		{
			for (i in this[cntnt[c]])
			{
				//Make a variable for the start coordinates of the current slot (shorter in the if)
				var slotCoordinates = this.slotCoordinates[this[cntnt[c]][i].stack.slot];
				/*Check if the Mouse is over the slot
				 >= to start coordinates
				 <= to start coordinates + the scaled size of the slot */
				if (x >= slotCoordinates.x && 
					y >= slotCoordinates.y &&
					x <= slotCoordinates.x + 16 * this.scale &&
					y <= slotCoordinates.y + 16 * this.scale)
				{
					//Mouse is over a filled slot
					return this[cntnt[c]][i].stack;
				}
			}
		}
		//If the Mouse is not over a filled slot, return false
		return false;
	};
	
	this.drawSkin = function()
	{
		if (this.skinImage.complete)
		{
			//drawImage(image, src_start_x, src_start_y, src_size_x, src_size_y, dest_start_x, dest_start_y, dest_size_x, dest_size_y);
			//draw the head
			this.context.drawImage(this.skinImage, 8, 8,  8, 8, 48 * this.scale, 27 * this.scale, 8 * this.scale, 8 * this.scale);
			//draw the body
			this.context.drawImage(this.skinImage, 20, 20, 8, 12, 48 * this.scale, 35 * this.scale, 8 * this.scale, 12 * this.scale);
			//draw the left leg
			this.context.drawImage(this.skinImage, 4,  20, 4, 12, 48 * this.scale, 47 * this.scale, 4 * this.scale, 12 * this.scale);
			//draw the right leg
			this.context.drawImage(this.skinImage, 4,  20, 4, 12, 52 * this.scale, 47 * this.scale, 4 * this.scale, 12 * this.scale);
			//draw the left arm
			this.context.drawImage(this.skinImage, 44, 20, 4, 12, 44 * this.scale, 35 * this.scale, 4 * this.scale, 12 * this.scale);
			//draw the right arm
			this.context.drawImage(this.skinImage, 52, 20, 4, 12, 56 * this.scale, 35 * this.scale, 4 * this.scale, 12 * this.scale);
		}
	};
	
	this.drawContent = function()
	{
		if (this.inventoryImage.complete)
		{
			//Draw Inventory Image, to scale
			this.context.drawImage(this.inventoryImage, 0, 0, 176 * this.scale, 166 * this.scale);
		}
		//Loop through all the filled slots and draw their image at the slot's coordinates; to scale
		for (i in this.content)
		{
			if (this.content[i].complete && this.content[i].animation == null)
			{
				this.context.drawImage(this.content[i], this.slotCoordinates[this.content[i].stack.slot].x, this.slotCoordinates[this.content[i].stack.slot].y, 16 * this.scale, 16 * this.scale);
			}
		}
	};
	
	this.drawAnimatedContent = function(ignore)
	{
		for (i in this.animatedContent)
		{
			var content = this.content[this.animatedContent[i]];
			if (content.complete)
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
				this.context.drawImage(content, 0, 16 + 16 * content.animation.currentFrame, 16, 16, this.slotCoordinates[content.stack.slot].x, this.slotCoordinates[content.stack.slot].y, 16 * this.scale, 16 * this.scale);
			}
		}
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
			//Make new slot coordinates for the new scale
			this.slotCoordinates = this.getSlotCoordinates(nScale);
			//Change the canvas dimensions to fit
			this.canvas.width = 176 * nScale;
			this.canvas.height = 166 * nScale;
			//Call the update function so it gets redrawn
			this.update();
		}
	};
	
	this.setPlayerName = function(name)
	{
		//If the new name is different from the old, change it
		if (name != this.playerName)
		{
			//Cache current inventory if it's not yet
			if (this.cachedInventories[this.playerName] == null)
			{
				this.cachedInventories[this.playerName] = {content: this.content, animatedContent: this.animatedContent, skinImage: this.skinImage};
			}
			
			//Get the inventory for the player
			if (this.cachedInventories[name] == null)
			{
				//Request from server if it's not in the cache
				this.getInventoryFor(name);
			}
			else
			{
				//Load it from the cache
				this.skinImage = this.cachedInventories[name].skinImage;
				this.content = this.cachedInventories[name].content;
				this.animatedContent = this.cachedInventories[name].animatedContent;
			}
			//Set the player name
			this.playerName = name;
			//Make the new skinURL
			this.skinURL = "http://s3.amazonaws.com/MinecraftSkins/" + name + ".png";
			this.skinImage.src = this.skinURL;
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
			xmlhttp.open("GET", "http://sp.svennp.com/invscan/php/getPlayerInventory.php?name=" + name + "&time=" + new Date().getTime());
			//Send the request
			xmlhttp.send();
	};
	
	this.setContent = function(nContent)
	{
		//new empty arrays for the new content(s)
		var nContentArray = [];
		var nAnimatedContentArray = [];
		//Loop through the stack-objects in the received array
		for (i in nContent)
		{
			nContent[i].slot--;
			var image = new Image();
				image.parent = this;
				image.stack = nContent[i];
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
	};
	
	this.update = function()
	{
		this.drawContent();
		this.drawAnimatedContent(true);
		this.drawSkin();
	};
	
	this.cachedInventories = {};
	this.scale = parseFloat(scale).toString() != "NaN" ? parseFloat(scale) : 1;
	this.canvas = document.getElementById(canvasId);
		this.canvas.width = 176 * this.scale;
		this.canvas.height = 166 * this.scale;
	this.context = this.canvas.getContext("2d");
	this.content = [];
	this.animatedContent = [];
	this.slotCoordinates = this.getSlotCoordinates(this.scale);
	this.playerName = "char";
	this.skinURL = "http://s3.amazonaws.com/MinecraftSkins/" + this.playerName + ".png";
	this.skinImage = new Image();
		this.skinImage.parent = this;
		this.skinImage.onload = function() { this.parent.update(); };
		this.skinImage.src = this.skinURL;
	this.inventoryImage = new Image();
		this.inventoryImage.parent = this;
		this.inventoryImage.onload = function() { this.parent.update(); };
		this.inventoryImage.src = "./texture/playerInventory.png";
}