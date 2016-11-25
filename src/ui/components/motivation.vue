/*
    Users need to make sure, height is >= text height. Otherwise it rendering clitches.
*/
<template>
    <div v-show="show" transition="fade">{{{messages[messageIndex]}}}</div>
</template>

<style>
.fade-transition {
    opacity: 1;
    transition: opacity .5s ease-in-out
}
.fade-enter,.fade-leave {
    opacity: 0;
}
</style>

<script>
const Vue = require('vue')

// Callback hook -> show next message, if fade transisiton reaches end.
Vue.transition('fade', {
  afterLeave: function (el) {
      this.cycleMessage();
  }
})

export default {
    props: {
        message: {
            type: String,
            default: ""
        }
    },
    data() {
        return {
            messages: [
                "Some of the money you lose will go into our pockets. The other part will be our earnings.",
                "Play now! Don't worry about such things as human dignitiy!",
                "Be this guys friends: <a href=\"https://www.youtube.com/watch?v=j9fwEHRS2nw\" target=\"_blank\">https://www.youtube.com/watch?v=j9fwEHRS2nw</a>",
                "Betting doesn't kill people, earthquakes do.",
                "Got earthquake?",
                "Let the misery of others not stop you from cashing out.",
                "Create a tsunami of money, washing you away.",
                "Don't let your dreams be shattered like houses.",
                "Close your financial gap by betting now."
            ],
            messageIndex: 0,
            show: true
        }
    },
    components: {
        'modal': require('vue-strap').modal
    },
    ready() {
        window.setInterval(this.triggerMessageCycle, 10000);
    },
    methods: {
        triggerMessageCycle() {
            this.show = false;
        },
        cycleMessage() {
            var index = this.messageIndex;
            index++;
            if (index >= this.messages.length) {
                index = 0;
            }
            this.messageIndex = index;
            this.show = true;
        }
    }
}
</script>
