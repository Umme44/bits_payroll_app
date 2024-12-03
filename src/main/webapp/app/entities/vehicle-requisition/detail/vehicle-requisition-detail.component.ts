import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVehicleRequisition } from '../vehicle-requisition.model';

@Component({
  selector: 'jhi-vehicle-requisition-detail',
  templateUrl: './vehicle-requisition-detail.component.html',
})
export class VehicleRequisitionDetailComponent implements OnInit {
  vehicleRequisition: IVehicleRequisition | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vehicleRequisition }) => {
      this.vehicleRequisition = vehicleRequisition;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
