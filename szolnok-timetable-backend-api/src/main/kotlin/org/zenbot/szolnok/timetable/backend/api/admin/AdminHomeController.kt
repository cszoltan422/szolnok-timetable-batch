package org.zenbot.szolnok.timetable.backend.api.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

@Controller
class AdminHomeController {

    @RequestMapping("/")
    fun homepage() : ModelAndView {
        val modelAndView = ModelAndView("/index")
        return modelAndView
    }
}