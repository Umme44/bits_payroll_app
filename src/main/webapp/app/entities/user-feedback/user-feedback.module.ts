import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { UserFeedbackComponent } from './list/user-feedback.component';
import { UserFeedbackDetailComponent } from './detail/user-feedback-detail.component';
import { UserFeedbackUpdateComponent } from './update/user-feedback-update.component';
import { UserFeedbackDeleteDialogComponent } from './delete/user-feedback-delete-dialog.component';
import { UserFeedbackRoutingModule } from './route/user-feedback-routing.module';

@NgModule({
  imports: [SharedModule, UserFeedbackRoutingModule],
  declarations: [UserFeedbackComponent, UserFeedbackDetailComponent, UserFeedbackUpdateComponent, UserFeedbackDeleteDialogComponent],
})
export class UserFeedbackModule {}
