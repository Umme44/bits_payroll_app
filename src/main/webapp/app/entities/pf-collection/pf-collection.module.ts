import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PfCollectionComponent } from './list/pf-collection.component';
import { PfCollectionDetailComponent } from './detail/pf-collection-detail.component';
import { PfCollectionUpdateComponent } from './update/pf-collection-update.component';
import { PfCollectionDeleteDialogComponent } from './delete/pf-collection-delete-dialog.component';
import { PfCollectionRoutingModule } from './route/pf-collection-routing.module';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';
import { NgSelectModule } from '@ng-select/ng-select';
import {BitsHrPayrollSelectYearModule} from "../../shared/select-year/select-year.module";
import {SharedLibsModule} from "../../shared/shared-libs.module";
import {
    BitsHrPayrollSimpleSelectEmployeeFormModule
} from "../../shared/simple-select-employee/simple-select-employee-form.module";
import {
  BitsHrPayrollSelectPfAccountFormModule
} from "../../shared/select-pf-account-form/select-pf-account-form.module";

@NgModule({
  imports: [SharedModule, PfCollectionRoutingModule, BitsHrPayrollHeaderModule, NgSelectModule, SharedLibsModule, BitsHrPayrollSelectYearModule, BitsHrPayrollSimpleSelectEmployeeFormModule, BitsHrPayrollSelectPfAccountFormModule],
  declarations: [PfCollectionComponent, PfCollectionDetailComponent, PfCollectionUpdateComponent, PfCollectionDeleteDialogComponent],
})
export class PfCollectionModule {}
