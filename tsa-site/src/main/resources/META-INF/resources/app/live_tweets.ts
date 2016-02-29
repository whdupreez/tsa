import {Component} from 'angular2/core';
import {NgZone} from 'angular2/core';
import {AgoPipe} from './ago_pipe';

@Component({
    // Declare the tag name in index.html to where the component attaches
    selector: '[live-tweets]',
    // Location of the template for this component
    templateUrl: 'app/live_tweets.html',
    pipes: [AgoPipe]
})
export class LiveTweets {

	zone: NgZone;
    tweets: any[] = [];

    constructor() {
    	this.zone = new NgZone({enableLongStackTrace: false});
        var source: any = new EventSource("twitter/sse");
		source.onmessage = (event) => {
			this.zone.run(() => {
                console.log(JSON.parse(event.data));
	            this.tweets.unshift(JSON.parse(event.data));
	            if (this.tweets.length > 10) {
	                this.tweets.splice(-1,1);
	            }
	        });
		};
    }

}
