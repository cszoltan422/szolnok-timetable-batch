package org.zenbot.szolnok.timetable.backend.api.admin

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import org.zenbot.szolnok.timetable.backend.api.configuration.properties.AdminProperties

@Controller
@EnableConfigurationProperties(AdminProperties::class)
class AdminHomeController(
    private val adminProperties: AdminProperties
) {

    @RequestMapping("/")
    fun homepage(): ModelAndView {
        val modelAndView = ModelAndView("index")
        modelAndView.addObject("applicationConsole", adminProperties.application.applicationConsole)
        return modelAndView
    }
}
