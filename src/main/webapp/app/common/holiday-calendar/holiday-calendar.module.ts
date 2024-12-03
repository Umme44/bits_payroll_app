import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { SharedModule } from '../../shared/shared.module';
import { HOLIDAY_CALENDAR_ROUTE, HolidayCalendarComponent } from './index';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';

@NgModule({
  imports: [SharedModule, RouterModule.forChild([HOLIDAY_CALENDAR_ROUTE]), BitsHrPayrollHeaderModule],
  declarations: [HolidayCalendarComponent],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class BitsHrPayrollAppHolidayCalendarModule {}
