package org.savvy.olympics.api

import org.springframework.http.MediaType
import org.springframework.web.service.annotation.HttpExchange


@HttpExchange("/home", accept = [MediaType.APPLICATION_JSON_VALUE])
interface HomeExchange {

}