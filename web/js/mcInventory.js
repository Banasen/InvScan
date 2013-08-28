countDown = function(times, onDone)
{
	this.times = times;
	this.current = times;
	this.onDone = onDone;
	this.decrement = function()
	{
		this.current--;
		if (this.current <= 0)
		{
			this.onDone();
		}
	};
	this.reset = function(nTimes)
	{
		if (nTimes != null)
		{
			this.times = nTimes;
		}
		this.current = this.times;
	};
};

mcInventory = function(canvasId, scale, name, content)
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
	};
	
	this.drawContent = function()
	{
		//Draw Inventory Image, to scale
		this.context.drawImage(this.inventoryImage, 0, 0, 176 * this.scale, 166 * this.scale);
		//Loop through all the filled slots and draw their image at the slot's coordinates, to scale
		for (i in this.content)
		{
			this.context.drawImage(this.content[i], this.slotCoordinates[this.content[i].stack.slot].x, this.slotCoordinates[this.content[i].stack.slot].y, 16 * this.scale, 16 * this.scale);
		}
	};
	
	this.setScale = function(nScale)
	{
		if (nScale != this.scale)
		{
			this.scale = nScale;
			this.slotCoordinates = this.getSlotCoordinates(nScale);
			this.canvas.width = 176 * nScale;
			this.canvas.height = 166 * nScale;
			this.update();
		}
	};
	
	this.setPlayerName = function(name)
	{
		if (name != this.playerName)
		{
			this.playerName = name;
			this.getInventoryFor(name);
			this.skinURL = "http://minecraft.net/skin/" + name + ".png";
			this.skinImage.src = this.skinURL;
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
			xmlhttp.open("GET", "./php/getInventory.php?name=" + name);
			//Send the request
			xmlhttp.send();
	};
	
	this.setContent = function(nContent)
	{
		var nContentArray = [];
		this.loadCountDown.reset(nContent.length);
		for (i in nContent)
		{
			nContent[i].slot--;
			var image = new Image();
				image.parent = this;
				image.stack = nContent[i];
				image.onload = function() { this.parent.loadCountDown.decrement(); };
				image.src = "./texture/" + nContent[i].itemRawName + ".png";
			nContentArray[nContentArray.length] = image;
		}
		this.content = nContentArray;
	};
	
	this.update = function()
	{
		this.drawContent();
		this.drawSkin();
	};
	
	this.loadCountDown = new countDown(40);
		this.loadCountDown.parent = this;
		this.loadCountDown.onDone = function() { this.parent.update(); };
	this.canvas = document.getElementById(canvasId);
		this.canvas.width = 176 * scale;
		this.canvas.height = 166 * scale;
	this.context = this.canvas.getContext("2d");
	this.scale = scale;
	this.content = content;
	this.slotCoordinates = this.getSlotCoordinates(scale);
	this.playerName = name;
	this.skinURL = "http://minecraft.net/images/char.png";
	this.skinImage = new Image();
		this.skinImage.parent = this;
		this.skinImage.onload = function() { this.parent.update(); };
		this.skinImage.src = this.skinURL;
	this.inventoryImage = new Image();
		this.inventoryImage.parent = this;
		this.inventoryImage.onload = function() { this.parent.update(); };
		this.inventoryImage.src = "./texture/inventory.png";
}