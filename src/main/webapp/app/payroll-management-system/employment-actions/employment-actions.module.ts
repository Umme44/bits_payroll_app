import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from '../../shared/shared.module';

import { EMPLOYMENT_ACTIONS_ROUTE, EmploymentActionsComponent } from './index';
import { OnboardComponent } from './onboard/onboard.component';
import { EmployeeDetailModalComponent } from './onboard/employee-detail-modal.component';

@NgModule({
  imports: [
    SharedModule,
    RouterModule.forChild([EMPLOYMENT_ACTIONS_ROUTE]),
    RouterModule.forChild([
      {
        path: 'employment-actions',
        loadChildren: () => import('./increment/increment.module').then(m => m.BitsHrPayrollIncrementModule),
      },
      {
        path: 'employment-actions',
        loadChildren: () => import('./promotion/promotion.module').then(m => m.BitsHrPayrollPromotionModule),
      },
      {
        path: 'employment-actions',
        loadChildren: () => import('./transfer/transfer.module').then(m => m.BitsHrPayrollTransferModule),
      },
      {
        path: 'employment-actions',
        loadChildren: () => import('./batch-operations/batch-operations.module').then(m => m.BitsHrPayrollBatchOperationsModule),
      },
    ]),
  ],
  declarations: [EmploymentActionsComponent, OnboardComponent, EmployeeDetailModalComponent],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class BitsHrPayrollAppEmploymentActionsModule {}
