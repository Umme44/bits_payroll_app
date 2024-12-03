import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CalenderYearComponent } from './list/calender-year.component';
import { CalenderYearDetailComponent } from './detail/calender-year-detail.component';
import { CalenderYearUpdateComponent } from './update/calender-year-update.component';
import { CalenderYearDeleteDialogComponent } from './delete/calender-year-delete-dialog.component';
import { CalenderYearRoutingModule } from './route/calender-year-routing.module';

@NgModule({
  imports: [SharedModule, CalenderYearRoutingModule],
  declarations: [CalenderYearComponent, CalenderYearDetailComponent, CalenderYearUpdateComponent, CalenderYearDeleteDialogComponent],
})
export class CalenderYearModule {}
