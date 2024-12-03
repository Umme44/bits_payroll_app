import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SalaryGeneratorMasterDetailComponent } from './salary-generator-master-detail.component';

describe('SalaryGeneratorMaster Management Detail Component', () => {
  let comp: SalaryGeneratorMasterDetailComponent;
  let fixture: ComponentFixture<SalaryGeneratorMasterDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SalaryGeneratorMasterDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ salaryGeneratorMaster: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SalaryGeneratorMasterDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SalaryGeneratorMasterDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load salaryGeneratorMaster on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.salaryGeneratorMaster).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
