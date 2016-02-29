System.register(['angular2/core'], function(exports_1) {
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
    var core_1, core_2;
    var Sentiment;
    return {
        setters:[
            function (core_1_1) {
                core_1 = core_1_1;
                core_2 = core_1_1;
            }],
        execute: function() {
            Sentiment = (function () {
                function Sentiment() {
                    var _this = this;
                    this.sentiments = [];
                    this.totalPos = 0;
                    this.totalNeg = 0;
                    this.totalNet = 0;
                    this.zone = new core_2.NgZone({ enableLongStackTrace: false });
                    var source = new EventSource("twitter/sentiment/sse");
                    source.onmessage = function (event) {
                        _this.zone.run(function () {
                            var sentiment = JSON.parse(event.data);
                            _this.totalPos += sentiment.pos;
                            _this.totalNeg += sentiment.neg;
                            _this.totalNet += sentiment.net;
                            sentiment.style = sentiment.net === 0 ? 'info' : sentiment.net > 0 ? 'success' : 'danger';
                            _this.sentiments.unshift(sentiment);
                            if (_this.sentiments.length > 10) {
                                _this.sentiments.splice(-1, 1);
                            }
                            var negVal = _this.totalNeg === 0 && _this.totalPos === 0 ? 1 : _this.totalNeg;
                            var posVal = _this.totalNeg === 0 && _this.totalPos === 0 ? 1 : _this.totalPos;
                            if (!_this.chart) {
                                var ctx = document.getElementById("sentimentChart").getContext("2d");
                                var data = [
                                    {
                                        value: negVal,
                                        color: "#F7464A",
                                        highlight: "#FF5A5E",
                                        label: "Red"
                                    },
                                    {
                                        value: posVal,
                                        color: "#5cb85c",
                                        highlight: "#5fff5c",
                                        label: "Green"
                                    }];
                                _this.chart = new Chart(ctx).Pie(data);
                            }
                            else {
                                _this.chart.segments[0].value = negVal;
                                _this.chart.segments[1].value = posVal;
                                _this.chart.update();
                            }
                        });
                    };
                }
                Sentiment = __decorate([
                    core_1.Component({
                        // Declare the tag name in index.html to where the component attaches
                        selector: '[sentiment]',
                        // Location of the template for this component
                        templateUrl: 'app/sentiment.html'
                    }), 
                    __metadata('design:paramtypes', [])
                ], Sentiment);
                return Sentiment;
            })();
            exports_1("Sentiment", Sentiment);
        }
    }
});
