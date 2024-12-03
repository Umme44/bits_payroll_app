import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { NationalityComponent } from './list/nationality.component';
import { NationalityDetailComponent } from './detail/nationality-detail.component';
import { NationalityUpdateComponent } from './update/nationality-update.component';
import { NationalityDeleteDialogComponent } from './delete/nationality-delete-dialog.component';
import { NationalityRoutingModule } from './route/nationality-routing.module';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';

@NgModule({
  imports: [SharedModule, NationalityRoutingModule, BitsHrPayrollHeaderModule],
  declarations: [NationalityComponent, NationalityDetailComponent, NationalityUpdateComponent, NationalityDeleteDialogComponent],
})
export class NationalityModule {}
