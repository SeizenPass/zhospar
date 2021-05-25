function toHumanReadable(timestamp) {
    let measurements = {
        w: 604800000,
        d: 86400000,
        h: 3600000,
        m: 60000,
        s: 1000
    }
    let milliSeconds = Date.parse(timestamp)
    let now = Date.now()
    let diff = now - milliSeconds
    let number = 0
    let string = ""
    if (diff > measurements.w) {
        number = Math.floor(diff / measurements.w)
        string = "недель"
    }
    else if (diff > measurements.d) {
        number = Math.floor(diff / measurements.d)
        string = "дня"
    }
    else if (diff > measurements.h) {
        number = Math.floor(diff / measurements.h)
        string = "час(а)"
    }
    else if (diff > measurements.m) {
        number = Math.floor(diff / measurements.m)
        string = "минут"
    }
    else if (diff > measurements.s) {
        number = Math.floor(diff / measurements.s)
        string = "секунд"
    }
    return number + " " + string + " назад"

}