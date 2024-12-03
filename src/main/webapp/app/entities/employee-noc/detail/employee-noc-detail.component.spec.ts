import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EmployeeNOCDetailComponent } from './employee-noc-detail.component';

describe('EmployeeNOC Management Detail Component', () => {
  let comp: EmployeeNOCDetailComponent;
  let fixture: ComponentFixture<EmployeeNOCDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EmployeeNOCDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ employeeNOC: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EmployeeNOCDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EmployeeNOCDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load employeeNOC on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.employeeNOC).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
