import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NgSelectModule } from '@ng-select/ng-select';
import { SharedModule } from '../../../shared/shared.module';
import { PROMOTION_ROUTE } from './promotion.route';
import { PromotionCreateComponent } from './promotion-create.component';
import { PromotionListComponent } from './promotion-list.component';
import { PromotionDetailComponent } from './promotion-detail.component';
import { PromotionDeleteComponent } from './promotion-delete.component';
import { PromotionUpdateComponent } from './promotion-update.component';
import { BitsHrPayrollSelectEmployeeFormModule } from '../../../shared/select-employee-form/select-employee-form.module';
import { BitsHrPayrollHeaderModule } from '../../../layouts/header/header.module';
import { BitsHrPayrollSimpleSelectEmployeeFormModule } from '../../../shared/simple-select-employee/simple-select-employee-form.module';

@NgModule({
  imports: [
    SharedModule,
    RouterModule.forChild(PROMOTION_ROUTE),
    BitsHrPayrollSelectEmployeeFormModule,
    BitsHrPayrollHeaderModule,
    BitsHrPayrollSimpleSelectEmployeeFormModule,
    NgSelectModule,
  ],
  declarations: [
    PromotionCreateComponent,
    PromotionListComponent,
    PromotionDetailComponent,
    PromotionDeleteComponent,
    PromotionUpdateComponent,
  ],
  entryComponents: [PromotionDeleteComponent],
})
export class BitsHrPayrollPromotionModule {}
