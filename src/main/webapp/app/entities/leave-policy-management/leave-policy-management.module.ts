import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsComponent } from './forms/forms.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';  // Import both
import { RouteRoutingModule } from './route/route-routing.module';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';




@NgModule({
  declarations: [
    FormsComponent,
  ],
  imports: [
    CommonModule,
    FormsModule,           // Add this
    ReactiveFormsModule,
    RouteRoutingModule
  ]
})
export class LeavePolicyManagementModule {








}
