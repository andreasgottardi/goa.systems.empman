function createXHR() {
	if (typeof XMLHttpRequest !== "undefined") {
		return new XMLHttpRequest()
	} else {
		var versions = ["MSXML2.XmlHttp.6.0", "MSXML2.XmlHttp.3.0"];
		for (var i = 0, length = versions.length; i < length; i++) {
			try {
				return new ActiveXObject(versions[i]);
			} catch (error) {
				console.error("Error creating XmlHttp object for Internet Explorer.")
			}
		}
	}
	return null;
}