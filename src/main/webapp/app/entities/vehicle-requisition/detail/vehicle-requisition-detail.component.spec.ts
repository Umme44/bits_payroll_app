import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { VehicleRequisitionDetailComponent } from './vehicle-requisition-detail.component';

describe('VehicleRequisition Management Detail Component', () => {
  let comp: VehicleRequisitionDetailComponent;
  let fixture: ComponentFixture<VehicleRequisitionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [VehicleRequisitionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ vehicleRequisition: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(VehicleRequisitionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(VehicleRequisitionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load vehicleRequisition on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.vehicleRequisition).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
