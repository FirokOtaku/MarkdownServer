package firok.mds.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IconController
{
	@GetMapping("favicon.ico")
	public String favicon()
	{
		return "redirect:/static/images/favicon.ico";
	}
}
