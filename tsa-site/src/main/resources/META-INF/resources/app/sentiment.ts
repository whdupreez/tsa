import {Component} from 'angular2/core';
import {NgZone} from 'angular2/core';

@Component({
    // Declare the tag name in index.html to where the component attaches
    selector: '[sentiment]',
    // Location of the template for this component
    templateUrl: 'app/sentiment.html'
})
export class Sentiment {

    zone: NgZone;
    sentiments: any[] = [];
    totalPos: number = 0;
    totalNeg: number = 0;
    totalNet: number = 0;

    chart: any;

    constructor() {
        this.zone = new NgZone({ enableLongStackTrace: false });

        var source: any = new EventSource("twitter/sentiment/sse");
        source.onmessage = (event) => {
            this.zone.run(() => {
                var sentiment: any = JSON.parse(event.data);
                this.totalPos += sentiment.pos;
                this.totalNeg += sentiment.neg;
                this.totalNet += sentiment.net;
                sentiment.style = sentiment.net === 0 ? 'info' : sentiment.net > 0 ? 'success' : 'danger';
                this.sentiments.unshift(sentiment);
                if (this.sentiments.length > 10) {
                    this.sentiments.splice(-1, 1);
                }
                var negVal = this.totalNeg === 0 && this.totalPos === 0 ? 1 : this.totalNeg;
                var posVal = this.totalNeg === 0 && this.totalPos === 0 ? 1 : this.totalPos;
                if (!this.chart) {
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
                    this.chart = new Chart(ctx).Pie(data);
                }
                else {
                    this.chart.segments[0].value = negVal;
                    this.chart.segments[1].value = posVal;
                    this.chart.update();
                }
            });
        };
    }

}
