import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { UnitOfMeasurementComponent } from './list/unit-of-measurement.component';
import { UnitOfMeasurementDetailComponent } from './detail/unit-of-measurement-detail.component';
import { UnitOfMeasurementUpdateComponent } from './update/unit-of-measurement-update.component';
import { UnitOfMeasurementDeleteDialogComponent } from './delete/unit-of-measurement-delete-dialog.component';
import { UnitOfMeasurementRoutingModule } from './route/unit-of-measurement-routing.module';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';

@NgModule({
  imports: [SharedModule, UnitOfMeasurementRoutingModule, BitsHrPayrollHeaderModule],
  declarations: [
    UnitOfMeasurementComponent,
    UnitOfMeasurementDetailComponent,
    UnitOfMeasurementUpdateComponent,
    UnitOfMeasurementDeleteDialogComponent,
  ],
})
export class UnitOfMeasurementModule {}
