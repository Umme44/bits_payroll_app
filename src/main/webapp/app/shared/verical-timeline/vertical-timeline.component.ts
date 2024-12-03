import { Component, Input } from '@angular/core';
import { Timeline } from '../legacy/legacy-model/timeline-model';

@Component({
  selector: 'jhi-vertical-timeline',
  templateUrl: './vertical-timeline.component.html',
  styleUrls: ['./vertical-timeline.component.scss'],
})
export class VerticalTimelineComponent {
  @Input()
  timelineList!: Timeline[];

  constructor() {}
}
