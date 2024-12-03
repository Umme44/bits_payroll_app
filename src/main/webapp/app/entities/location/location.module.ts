import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LocationComponent } from './location.component';
import { LocationDetailComponent } from './location-detail.component';
import { LocationUpdateComponent } from './location-update.component';
import { LocationDeleteDialogComponent } from './location-delete-dialog.component';
import { locationRoute } from './location.route';
import { BitsHrPayrollHeaderModule } from 'app/layouts/header/header.module';
import { SharedModule } from '../../shared/shared.module';

@NgModule({
  imports: [SharedModule, RouterModule.forChild(locationRoute), BitsHrPayrollHeaderModule],
  declarations: [LocationComponent, LocationDetailComponent, LocationUpdateComponent, LocationDeleteDialogComponent],
  entryComponents: [LocationDeleteDialogComponent],
})
export class BitsHrPayrollLocationModule {}
