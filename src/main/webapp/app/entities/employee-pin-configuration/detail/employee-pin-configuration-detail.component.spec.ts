import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EmployeePinConfigurationDetailComponent } from './employee-pin-configuration-detail.component';

describe('EmployeePinConfiguration Management Detail Component', () => {
  let comp: EmployeePinConfigurationDetailComponent;
  let fixture: ComponentFixture<EmployeePinConfigurationDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EmployeePinConfigurationDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ employeePinConfiguration: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EmployeePinConfigurationDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EmployeePinConfigurationDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load employeePinConfiguration on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.employeePinConfiguration).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
