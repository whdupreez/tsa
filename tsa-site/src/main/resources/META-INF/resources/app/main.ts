import {Component} from 'angular2/core';
import {bootstrap}  from 'angular2/platform/browser';
import {LiveTweets} from './live_tweets';
import {Sentiment} from './sentiment';

@Component({
    selector: '[main-app]',
    templateUrl: 'app/main.html',
    directives: [LiveTweets, Sentiment]
})
export class Main {
}

bootstrap(Main);
