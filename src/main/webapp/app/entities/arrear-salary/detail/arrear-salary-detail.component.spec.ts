import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ArrearSalaryDetailComponent } from './arrear-salary-detail.component';

describe('ArrearSalary Management Detail Component', () => {
  let comp: ArrearSalaryDetailComponent;
  let fixture: ComponentFixture<ArrearSalaryDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ArrearSalaryDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ arrearSalary: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ArrearSalaryDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ArrearSalaryDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load arrearSalary on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.arrearSalary).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
