import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EmployeePinDetailComponent } from './employee-pin-detail.component';

describe('EmployeePin Management Detail Component', () => {
  let comp: EmployeePinDetailComponent;
  let fixture: ComponentFixture<EmployeePinDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EmployeePinDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ employeePin: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EmployeePinDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EmployeePinDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load employeePin on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.employeePin).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
