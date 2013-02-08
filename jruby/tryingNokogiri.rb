require 'rubygems'
require 'nokogiri'
require 'open-uri'


class Tester

	def test() 

		page = Nokogiri::HTML(open("http://ruby.bastardsbook.com/files/hello-webpage.html"))
		puts page.css("title")[0].name
		puts page.css("title")[0].text
		puts page.xpath("//html//body//div[@id='references']//p//a")

	end

end