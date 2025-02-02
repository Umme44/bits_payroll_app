import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DesignationComponent } from './list/designation.component';
import { DesignationDetailComponent } from './detail/designation-detail.component';
import { DesignationUpdateComponent } from './update/designation-update.component';
import { DesignationDeleteDialogComponent } from './delete/designation-delete-dialog.component';
import { DesignationRoutingModule } from './route/designation-routing.module';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';

@NgModule({
  imports: [SharedModule, DesignationRoutingModule, BitsHrPayrollHeaderModule],
  declarations: [DesignationComponent, DesignationDetailComponent, DesignationUpdateComponent, DesignationDeleteDialogComponent],
})
export class DesignationModule {}
