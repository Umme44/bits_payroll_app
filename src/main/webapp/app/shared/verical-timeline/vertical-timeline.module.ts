import { NgModule } from '@angular/core';

import { SharedModule } from '../shared.module';
import { NgSelectModule } from '@ng-select/ng-select';
import { VerticalTimelineComponent } from './vertical-timeline.component';

@NgModule({
  imports: [NgSelectModule, SharedModule],
  declarations: [VerticalTimelineComponent],
  entryComponents: [VerticalTimelineComponent],
  exports: [VerticalTimelineComponent],
})
export class VerticalTimelineModule {}
