inventoryNames = {"chest"}

Args = {...}

if not Args[1] then
	error("Name must be given! Usage: chestScan name [playerName]")
end

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

baseURL = "http://sp.svennp.com/invscan/php/postChest.php?name="

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
	print("Starting http-request!")
	http.request(baseURL..Args[1].."&playerName="..Args[2], postInfo)
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