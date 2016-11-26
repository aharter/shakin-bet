<template>
    <div class="site-wrapper">
      <div class="site-wrapper-inner">
        <div class="masthead clearfix">
            <div class="inner">
                <motivation style="font-size: 25px;"></motivation>
            </div>
        </div>
        <!-- Betting page -->
        <h1 class="covers-heading" v-if="state.placeBet">Bet on the magnitude of the next earthquake!</h1>
        <div class="cover-container" v-if="state.placeBet">
          <div class="inner cover">
            <p class="lead">
                <form class="form-inliner row">
                    <div class="col-md-1"></div>
                    <div class="form-group col-md-3">
                        <label for="creditInput">Your Credit ($):</label>
                        <input type="number" class="form-control" id="creditInput" size="4" readonly v-model="credit">
                    </div>
                    <div class="form-group col-md-4 slider-container">
                        <vue-slider v-ref:slider :tooltip="false" :value.sync="bet" :max.sync="credit"></vue-slider>
                    </div>
                    <div class="form-group col-md-3">
                        <label for="betInput">Your bet ($):</label>
                        <input type="number" class="form-control" id="betInput" v-model="bet">
                    </div>
                    <div class="col-md-1"></div>
                </form>
            </p>
            <p class="lead">
                <form class="form-inliner row">
                    <div class="form-group col-md-4"></div>
                    <div class="form-group col-md-4 slider-container">
                        <vue-slider v-ref:magnitudeSlider :value.sync="magnitudeBet" :tooltip="false" :min.sync="20" :max.sync="100"></vue-slider>
                    </div>
                    <div class="form-group col-md-3">
                        <label for="magnitudeInput">Richter scale:</label>
                        <input type="number" class="form-control" id="magnitudeInput" step="0.1" max="10" v-model="magnitudeBetForm">
                    </div>
                </form>
            </p>
            <p class="lead">
              <button v-on:click="placeBet()" class="btn btn-success" role="button" :class="{'disabled': bet <= 0}">Place bet</button>
            </p>
            <h1 class="cover-heading">Next bet in:</h1>
            <p class="lead"><countdown :date="nextBet" :callback="timerCallback"></countdown></p>
          </div>
        </div>
      </div>
    </div>
</template>

<style>
.slider-container {
    padding-top: 30px;
}

/*
 * Base structure
 */
html,
body,
#app {
  height: 100%;
  background-color: #333;
}
body {
  color: #fff;
  text-align: center;
  text-shadow: 0 1px 3px rgba(0,0,0,.5);
}

/* Extra markup and styles for table-esque vertical and horizontal centering */
.site-wrapper {
  display: table;
  width: 100%;
  height: 100%; /* For at least Firefox */
  min-height: 100%;
  -webkit-box-shadow: inset 0 0 100px rgba(0,0,0,.5);
          box-shadow: inset 0 0 100px rgba(0,0,0,.5);
}
.site-wrapper-inner {
  display: table-cell;
  vertical-align: top;
}
.cover-container {
  margin-right: auto;
  margin-left: auto;
}

/* Padding for spacing */
.inner {
  padding: 30px;
}

/*
 * Cover
 */
.cover {
  padding: 0 20px;
}
.cover .btn-lg {
  padding: 10px 20px;
  font-weight: bold;
}

/*
 * Affix and center
 */
@media (min-width: 768px) {
    /* Pull out the header and footer */
    .masthead {
        position: fixed;
        top: 0;
    }
    /* Start the vertical centering */
    .site-wrapper-inner {
        vertical-align: middle;
    }
    /* Handle the widths */
    .masthead,
    .cover-container {
        width: 100%; /* Must be percentage or pixels for horizontal alignment */
    }
}

@media (min-width: 992px) {
    .cover-container {
        width: 700px;
    }
}
</style>

<script>
var Vue = require('vue')
Vue.use(require('vue-resource'))

export default {
    data() {
        return {
            state: {
                placeBet: true
            },
            // current bet
            bet: 0,
            // max credit
            credit: 500,
            // slider component does not allow 0.1 steps, so we need to work around it
            // minium magnitude is set to 2.0
            magnitudeBet: 20,
            magnitudeBetForm: 2,
            //set date to "NaN" to get
            nextBet: new Date().setSeconds(NaN)
        }
    },
    ready: function(){
        this.fetchNextBetTime();
    },
    watch: {
        magnitudeBet: function(newVal, oldVal) {
            // transform range 0..100 to 0.0..10.0
            this.magnitudeBetForm = newVal / 10;
        },
        magnitudeBetForm: function(newVal, oldVal) {
            // transform range 0.0..10.0 to 0..100
            this.magnitudeBet = newVal * 10;
        }
    },
    components: {
        'vue-slider': require('vue-slider-component/src/vue-slider.vue'),
        'countdown': require('./components/Countdown.vue'),
        'motivation': require('./components/motivation.vue')
    },
    methods: {
        fetchNextBetTime: function() {
            var self = this;
            this.$http.get("/v1/next-bet-timestamp").then(function(response) {
                const time = response['data']['timestamp'];
                this.$set('nextBet', new Date(time));
            }, function(resp) {
                setTimeout(self.fetchNextBetTime, 1000);
                this.$set('nextBet', new Date(0));
            })
        },
        placeBet: function() {
            this.state.placeBet = false;
        },
        // Callback if timer reaches 0.
        timerCallback: function() {
            if (this.state.placeBet) {
                this.fetchNextBetTime();
                //TODO: show message "you missed last bet, but a new one is coming" for X seconds
            }
        }
    }
}
</script>
