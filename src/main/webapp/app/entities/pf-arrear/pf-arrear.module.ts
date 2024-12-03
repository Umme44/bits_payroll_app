import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PfArrearComponent } from './list/pf-arrear.component';
import { PfArrearDetailComponent } from './detail/pf-arrear-detail.component';
import { PfArrearUpdateComponent } from './update/pf-arrear-update.component';
import { PfArrearDeleteDialogComponent } from './delete/pf-arrear-delete-dialog.component';
import { PfArrearRoutingModule } from './route/pf-arrear-routing.module';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';
import { BitsHrPayrollSimpleSelectEmployeeFormModule } from '../../shared/simple-select-employee/simple-select-employee-form.module';
import { BitsHrPayrollSearchTextBoxModule } from '../../shared/search-text-box/search-text-box.module';

@NgModule({
  imports: [
    SharedModule,
    PfArrearRoutingModule,
    BitsHrPayrollHeaderModule,
    BitsHrPayrollSimpleSelectEmployeeFormModule,
    BitsHrPayrollSearchTextBoxModule,
  ],
  declarations: [PfArrearComponent, PfArrearDetailComponent, PfArrearUpdateComponent, PfArrearDeleteDialogComponent],
})
export class PfArrearModule {}
