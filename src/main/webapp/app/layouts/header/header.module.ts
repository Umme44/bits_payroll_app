import { NgModule } from '@angular/core';
import { HeaderComponent } from './header.component';
import { RouterModule } from '@angular/router';
import { SharedModule } from '../../shared/shared.module';

@NgModule({
  imports: [SharedModule, RouterModule],
  declarations: [HeaderComponent],
  entryComponents: [HeaderComponent],
  exports: [HeaderComponent],
})
export class BitsHrPayrollHeaderModule {}
