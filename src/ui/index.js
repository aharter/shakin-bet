var Vue = require('vue')
var VueRouter = require('vue-router')

var App = Vue.extend()

Vue.use(VueRouter);

var router = new VueRouter()
router.map({
    '/': {
        component: require('./place-bet.vue')
    },
})
router.start(App, '#app')
