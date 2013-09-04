parseAnimation = function(animationInfo)
{
	var animation = {frames: []};
	//Each line is a string in the Array
	for (i in animationInfo)
	{
		//Check if the object has a split method (only if it's a string)
		if (animationInfo[i].split != null)
		{
			//Split the string into 2 parts. first one is the frame number, next the duration, default duration is 1. format (frameNo[*duration])
			var info = animationInfo[i].split("*");
			//Frame number is zero-indexed
			if (parseInt(info[0]) >= 0)
			{
				//Add the frame to the array. if the duration is set in the info, use that, otherwise default to 1
				animation.frames[animation.frames.length] = {sprite: parseInt(info[0]), duration: parseInt(info[1]) >= 0 ? parseInt(info[1]) : 1};
			}
		}
	}
	//Make it start with the first frame of the animation (matters for long duration animations)
	animation.currentFrame = animation.frames.length - 1; //Set the currentFrame to the last frame in the animation
	animation.currentFrameDuration = animation.frames[animation.currentFrame].duration - 1; //Set the current frame duration to the last before it's over
	
	return animation;
};

mcInventory = function(canvasId, scale, name, content, animatedContent)
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
	}
	
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
			if (this.content[i].complete)
			{
				this.context.drawImage(this.content[i], this.slotCoordinates[this.content[i].stack.slot].x, this.slotCoordinates[this.content[i].stack.slot].y, 16 * this.scale, 16 * this.scale);
			}
		}
	};
	
	this.drawAnimatedContent = function(ignore)
	{
		for (i in this.animatedContent)
		{
			if (this.animatedContent[i].complete)
			{
				if (!ignore)
				{
					//Increment the duration of the current frame
					this.animatedContent[i].animation.currentFrameDuration++;
					//If the duration is (higher or) equal to the duration it should stay, go draw the next frame and go to the next one
					if (this.animatedContent[i].animation.currentFrameDuration >= this.animatedContent[i].animation.frames[this.animatedContent[i].animation.currentFrame].duration)
					{
						//Increment the currentFrame or revert it to the first when at the end
						this.animatedContent[i].animation.currentFrame = (this.animatedContent[i].animation.currentFrame + 1) % this.animatedContent[i].animation.frames.length;
						this.animatedContent[i].animation.currentFrameDuration = 0;
					}
				}
				//draw the new frame to the canvas
				//drawImage(image, src_start_x, src_start_y, src_size_x, src_size_y, dest_start_x, dest_start_y, dest_size_x, dest_size_y);
				this.context.drawImage(this.animatedContent[i], 0, 16 + 16 * this.animatedContent[i].animation.currentFrame, 16, 16, this.slotCoordinates[this.animatedContent[i].stack.slot].x, this.slotCoordinates[this.animatedContent[i].stack.slot].y, 16 * this.scale, 16 * this.scale);
			}
		}
	};
	
	this.setScale = function(nScale)
	{
		//If the new scale is different from the old, change it
		if (nScale != this.scale)
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
			xmlhttp.open("GET", "http://sp.svennp.com/invscan/php/getInventory.php?name=" + name + "&time=" + new Date().getTime());
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
				image.src = "http://sp.svennp.com/invscan/texture/" + nContent[i].itemRawName + ".png";
			if (nContent[i].animation != null && nContent[i].animation != "")
			{
				image.animation = parseAnimation(nContent[i].animation);
				nAnimatedContentArray[nAnimatedContentArray.length] = image;
			}
			else
			{
				nContentArray[nContentArray.length] = image;
			}
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
	
	name = name.toString() != "" ? name : "char";
	
	this.cachedInventories = {};
	this.canvas = document.getElementById(canvasId);
		this.canvas.width = 176 * scale;
		this.canvas.height = 166 * scale;
	this.context = this.canvas.getContext("2d");
	this.scale = parseFloat(scale).toString() != "NaN" ? parseFloat(scale) : 1;
	this.content = content != null ? content : [];
	this.animatedContent = animatedContent != null ? animatedContent : [];
	this.slotCoordinates = this.getSlotCoordinates(scale);
	this.playerName = name;
	this.skinURL = "http://s3.amazonaws.com/MinecraftSkins/" + name + ".png";
	this.skinImage = new Image();
		this.skinImage.parent = this;
		this.skinImage.onload = function() { this.parent.update(); };
		this.skinImage.src = this.skinURL;
	this.inventoryImage = new Image();
		this.inventoryImage.parent = this;
		this.inventoryImage.onload = function() { this.parent.update(); };
		this.inventoryImage.src = "http://sp.svennp.com/invscan/texture/inventory.png";
}