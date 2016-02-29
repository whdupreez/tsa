System.register(['angular2/core', 'angular2/platform/browser', './live_tweets', './sentiment'], function(exports_1) {
    "use strict";
    var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
        var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
        if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
        else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
        return c > 3 && r && Object.defineProperty(target, key, r), r;
    };
    var __metadata = (this && this.__metadata) || function (k, v) {
        if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
    };
    var core_1, browser_1, live_tweets_1, sentiment_1;
    var Main;
    return {
        setters:[
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (browser_1_1) {
                browser_1 = browser_1_1;
            },
            function (live_tweets_1_1) {
                live_tweets_1 = live_tweets_1_1;
            },
            function (sentiment_1_1) {
                sentiment_1 = sentiment_1_1;
            }],
        execute: function() {
            Main = (function () {
                function Main() {
                }
                Main = __decorate([
                    core_1.Component({
                        selector: '[main-app]',
                        templateUrl: 'app/main.html',
                        directives: [live_tweets_1.LiveTweets, sentiment_1.Sentiment]
                    }), 
                    __metadata('design:paramtypes', [])
                ], Main);
                return Main;
            })();
            exports_1("Main", Main);
            browser_1.bootstrap(Main);
        }
    }
});
