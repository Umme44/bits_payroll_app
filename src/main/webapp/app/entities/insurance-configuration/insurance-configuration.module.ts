import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { InsuranceConfigurationComponent } from './list/insurance-configuration.component';
import { InsuranceConfigurationDetailComponent } from './detail/insurance-configuration-detail.component';
import { InsuranceConfigurationUpdateComponent } from './update/insurance-configuration-update.component';
import { InsuranceConfigurationDeleteDialogComponent } from './delete/insurance-configuration-delete-dialog.component';
import { InsuranceConfigurationRoutingModule } from './route/insurance-configuration-routing.module';

@NgModule({
  imports: [SharedModule, InsuranceConfigurationRoutingModule],
  declarations: [
    InsuranceConfigurationComponent,
    InsuranceConfigurationDetailComponent,
    InsuranceConfigurationUpdateComponent,
    InsuranceConfigurationDeleteDialogComponent,
  ],
})
export class InsuranceConfigurationModule {}
