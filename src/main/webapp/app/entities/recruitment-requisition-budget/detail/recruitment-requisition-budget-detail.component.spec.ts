import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RecruitmentRequisitionBudgetDetailComponent } from './recruitment-requisition-budget-detail.component';

describe('RecruitmentRequisitionBudget Management Detail Component', () => {
  let comp: RecruitmentRequisitionBudgetDetailComponent;
  let fixture: ComponentFixture<RecruitmentRequisitionBudgetDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RecruitmentRequisitionBudgetDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ recruitmentRequisitionBudget: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RecruitmentRequisitionBudgetDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RecruitmentRequisitionBudgetDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load recruitmentRequisitionBudget on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.recruitmentRequisitionBudget).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
