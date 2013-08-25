os.loadAPI("ocs/apis/sensor")
p = sensor.wrap("front")
rs.setOutput("top", false)
while true do
	url = "http://sp.svennp.com/invscan/php/post.php?"
	t = p.getTargets()
	if #t then
		for a,b in pairs(t) do
			td = nil
			td = p.getTargetDetails(a)
			if td.RawName ~= nil then
				if math.floor(td.Position.Y) == 1 and math.floor(td.Position.X) == 0 and math.floor(td.Position.Z) == 1 then
					rs.setOutput("top", true)
					print("making url to post")
					for i = 1,40 do
						if td.Inventory[i].RawName ~= nil then
							url = url.."item["..i.."][name]="..td.Inventory[i].RawName.."&"
							url = url.."item["..i.."][size]="..td.Inventory[i].Size.."&"
						end
					end
					url = url.."name="..a
					print("sending info")
					sleep(0.2)
					http.get(url)
					rs.setOutput("top", false)
					sleep(2)
				end
			end
		end
	end
end