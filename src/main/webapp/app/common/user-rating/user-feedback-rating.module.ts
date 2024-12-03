import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { SharedModule } from '../../shared/shared.module';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';
import { UserFeedbackRatingComponent } from './user-feedback-rating.component';
import { USER_FEEDBACK_RATING_ROUTE } from './user-feedback-rating.route';

@NgModule({
  imports: [SharedModule, RouterModule.forChild([USER_FEEDBACK_RATING_ROUTE]), BitsHrPayrollHeaderModule],
  declarations: [UserFeedbackRatingComponent],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class BitsHrPayrollAppUserFeedbackRatingModule {}
