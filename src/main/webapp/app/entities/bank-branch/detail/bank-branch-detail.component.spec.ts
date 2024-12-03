import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BankBranchDetailComponent } from './bank-branch-detail.component';

describe('BankBranch Management Detail Component', () => {
  let comp: BankBranchDetailComponent;
  let fixture: ComponentFixture<BankBranchDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BankBranchDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ bankBranch: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(BankBranchDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(BankBranchDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load bankBranch on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.bankBranch).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
