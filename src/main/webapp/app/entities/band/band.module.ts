import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BandComponent } from './list/band.component';
import { BandDetailComponent } from './detail/band-detail.component';
import { BandUpdateComponent } from './update/band-update.component';
import { BandDeleteDialogComponent } from './delete/band-delete-dialog.component';
import { BandRoutingModule } from './route/band-routing.module';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';

@NgModule({
  imports: [SharedModule, BandRoutingModule, BitsHrPayrollHeaderModule],
  declarations: [BandComponent, BandDetailComponent, BandUpdateComponent, BandDeleteDialogComponent],
})
export class BandModule {}
