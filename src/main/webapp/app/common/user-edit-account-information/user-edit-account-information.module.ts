import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from '../../shared/shared.module';

import { USER_EDIT_ACCOUNT_INFORMATION_ROUTE, UserEditAccountInformationComponent } from './index';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';

@NgModule({
  imports: [
    SharedModule,
    RouterModule.forRoot([USER_EDIT_ACCOUNT_INFORMATION_ROUTE], { useHash: true }),
    BitsHrPayrollHeaderModule,
  ],
  declarations: [UserEditAccountInformationComponent],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class BitsHrPayrollAppUserEditAccountInformationModule {}
