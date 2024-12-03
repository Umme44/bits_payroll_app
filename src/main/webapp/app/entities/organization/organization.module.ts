import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { OrganizationComponent } from './list/organization.component';
import { OrganizationDetailComponent } from './detail/organization-detail.component';
import { OrganizationUpdateComponent } from './update/organization-update.component';
import { OrganizationDeleteDialogComponent } from './delete/organization-delete-dialog.component';
import { OrganizationRoutingModule } from './route/organization-routing.module';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';
import { NgSelectModule } from '@ng-select/ng-select';

@NgModule({
  imports: [SharedModule, OrganizationRoutingModule, NgSelectModule, BitsHrPayrollHeaderModule],
  declarations: [OrganizationComponent, OrganizationDetailComponent, OrganizationUpdateComponent, OrganizationDeleteDialogComponent],
})
export class OrganizationModule {}
