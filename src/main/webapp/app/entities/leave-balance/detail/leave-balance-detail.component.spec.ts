import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LeaveBalanceDetailComponent } from './leave-balance-detail.component';

describe('LeaveBalance Management Detail Component', () => {
  let comp: LeaveBalanceDetailComponent;
  let fixture: ComponentFixture<LeaveBalanceDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LeaveBalanceDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ leaveBalance: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(LeaveBalanceDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LeaveBalanceDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load leaveBalance on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.leaveBalance).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
