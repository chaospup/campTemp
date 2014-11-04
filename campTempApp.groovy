/**
 *  Camp Temp Log
 *
 *  Copyright 2014 Joe da Silva
 *
 */
definition(
    name: "Camp Temp Log",
    namespace: "",
    author: "Joe da Silva",
    description: "log the camper temperature to ThingSpeak",
    category: "My Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	section("Log the temperature from this temperature sensor #1...") {
		input "temp1", "capability.temperatureMeasurement", title: "Where?"
	}

        section( "Enter your ThingSpeak API Key..." ) {
		input "thingSpeakApiKey", "text"
        }
}

def installed() {
	log.debug "Installed with settings: ${settings}"
	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"
	unsubscribe()
	initialize()
}

def initialize() {
	subscribe( temp1, "temperature", handleTemperatureEvent );
}

def handleTemperatureEvent( evt ) {
	def httpGetUrl = "http://api.thingspeak.com/update?key=${thingSpeakApiKey}&field1=${evt.value}"
        httpGet( httpGetUrl ) { response -> 
    	if ( response.status != 200 ) {
        	log.debug( "ThingSpeak Logging Failed: status was ${response.status}" )
        } else {
        	log.debug( "ThingSpeak Logging Successful" )
        }
    }
}
