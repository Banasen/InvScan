sensorr = false
if fs.exists("ocs/apis/sensor") then
os.loadAPI("ocs/apis/sensor")
for _,s in pairs(rs.getSides()) do
        if peripheral.getType(s) == "sensor" then
          proxSensor = sensor.wrap(s)
		  sensorr = true
        end
  end
end
baseUrl = "http://sp.svennp.com/invscan/php/"
Args = {...}

if not Args[2] and Args[1] then
   error("chestname must be given! Usage: invscan chest(true/false) chestname [playerName](if chest)")
elseif not Args[3] and Args[1] then
   error("playername must be given! Usage: invscan chest(true/false) chestname [playerName](if chest)")
elseif not Args[1] and not sensorr or proxSensor.getSensorName() ~= "proximityCard"then
   error("Tried to scan players without sensor and Proximitycard attached.....")
end

local function httpreq(bUrl,postInfo)
print("Starting http-request!")
    http.request(bUrl, postInfo)
	completed = false
	repeat
		local event, url, sourceText = os.pullEvent()
		if event == "http_success" then
			print("Success!")
			completed = true
		elseif event == "http_failure" then
			print("Failure!")
			completed = true
		end
	until completed
	sleep(20)
end

if sensorr and not Args[1] then --if sensor and not chest then
while true do
	targets = proxSensor.getTargets()
	for name, information in pairs(targets) do
		if information.RawName == "net.minecraft.entity.player.EntityPlayerMP" then
			print("Player ["..name.."] found!")
			local details = proxSensor.getTargetDetails(name)
			local postInfo = ""
			print("Creating info string!")
			for slot, stack in pairs(details.Inventory) do
				if stack.RawName then
					postInfo = postInfo.."item["..slot.."][rawName]="..stack.RawName.."&"
					postInfo = postInfo.."item["..slot.."][name]="..stack.Name.."&"
					postInfo = postInfo.."item["..slot.."][size]="..stack.Size.."&"
				end
			end
			httpreq(baseUrl.."postChest.php?name="..name, postInfo)
		end
	end
	sleep(4)
end
end

if Args[1] then -- if chest then  (other args where checked at the start of the program)
inventoryNames = {"chest"}
inventories = {}
print("Looking for inventories...")
for _, side in pairs(peripheral.getNames()) do
	for __, inventoryName in pairs(inventoryNames) do
		if string.find(side, inventoryName) or string.find(peripheral.getType(side), inventoryName) then
			inventories[#inventories + 1] = peripheral.wrap(side)
			print(" Added "..inventories[#inventories].getInvName())
		end
	end
end

while true do
	print("Creating info string...")
	postInfo = ""
	i = 0
	for _, inventory in pairs(inventories) do
		print(" Adding info string for "..inventory.getInvName())
		for slot = 0, inventory.getSizeInventory() - 1, 1 do
			stack = inventory.getStackInSlot(slot)
			if stack then
				postInfo = postInfo.."item["..i.."][id]="..stack.id.."&"
				postInfo = postInfo.."item["..i.."][damage]="..stack.dmg.."&"
				postInfo = postInfo.."item["..i.."][rawName]="..stack.rawName.."&"
				postInfo = postInfo.."item["..i.."][name]="..stack.name.."&"
				postInfo = postInfo.."item["..i.."][size]="..stack.qty.."&"
				i = i + 1
			end
		end
	end
	httpreq(baseUrl.."postPlayer.php?name="..Args[2].."&playerName="..Args[3],postInfo)
end
end