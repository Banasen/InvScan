os.loadAPI("ocs/apis/sensor")
proxSensor = sensor.wrap("front")

baseURL = "http://sp.svennp.com/invscan/php/post.php?"

rs.setOutput("top", false)
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
			-- Add more info?
			print("Starting http-request!")
			http.request(baseURL.."name="..name, postInfo)
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
		end
	end
	sleep(4)
end