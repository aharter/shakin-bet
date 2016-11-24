/*
    Copied from https://github.com/fareez-ahamed/countdown-vuejs
    Licensed under MIT license. See Countdown-LICENSE for full license.
    Stops and triggers callback once, if timer reaches 00:00.
    Setting date will enable trigger again.
*/
<template>
    <div class="row">
        <div class="col-md-3"></div>
        <div class="col-md-3">
            <p class="digit">{{ minutes.toString() | leftPad 2 '0' }}</p>
            <p class="text">Minutes</p>
        </div>
        <div class="col-md-3">
            <p class="digit">{{ seconds.toString() | leftPad 2 '0' }}</p>
            <p class="text">Seconds</p>
        </div>
    </div>
</template>

<style>
@import url(https://fonts.googleapis.com/css?family=Roboto+Condensed:400|Roboto:100);

.text {
    font-size: 30px;
    font-family: 'Roboto Condensed', serif;
    font-weight: 400;
    margin-top: 5px;
    margin-bottom: 5px;
    text-align: center;
}

.digit {
    font-size: 80px;
    font-weight: 100;
    font-family: 'Roboto', serif;
    margin: 5px;
    text-align: center;
}
</style>

<script>
const Vue = require('vue')
Vue.use(require('vue-filter'))

export default {
    ready() {
        window.setInterval(() => {
            this.now = Math.trunc((new Date()).getTime() / 1000);
        }, 1000);
    },
    props : {
        date : {
            type: Number,
            coerce: d => Math.trunc(d / 1000)
        },
        callback : {
            type: Function,
            default: function() {}
        }
    },
    data() {
        return {
            now: Math.trunc((new Date()).getTime() / 1000),
            callbackTriggered: false
        }
    },
    watch: {
        // reset callback trigger if time changed
        date: function(newVal, oldVal) {
            this.callbackTriggered = false;
        }
    },
    computed: {
        seconds() {
            var sec = (this.date - this.now) % 60;
            if (sec <= 0 && this.minutes <= 0) {
                sec = 0;
                // trigger callback
                if (!this.callbackTriggered) {
                    this.callbackTriggered = true;
                    this.callback();
                }
            }
            return sec;
        },

        minutes() {
            var min = Math.trunc((this.date - this.now) / 60) % 60;
            if (min <= 0) {
                min = 0;
            }
            return min;
        }
    }
}
</script>
