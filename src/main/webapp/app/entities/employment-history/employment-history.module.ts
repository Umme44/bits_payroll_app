import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EmploymentHistoryComponent } from './list/employment-history.component';
import { EmploymentHistoryDetailComponent } from './detail/employment-history-detail.component';
import { EmploymentHistoryUpdateComponent } from './update/employment-history-update.component';
import { EmploymentHistoryDeleteDialogComponent } from './delete/employment-history-delete-dialog.component';
import { EmploymentHistoryRoutingModule } from './route/employment-history-routing.module';

@NgModule({
  imports: [SharedModule, EmploymentHistoryRoutingModule],
  declarations: [
    EmploymentHistoryComponent,
    EmploymentHistoryDetailComponent,
    EmploymentHistoryUpdateComponent,
    EmploymentHistoryDeleteDialogComponent,
  ],
})
export class EmploymentHistoryModule {}
