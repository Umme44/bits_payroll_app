import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MovementEntryDetailComponent } from './movement-entry-detail.component';

describe('MovementEntry Management Detail Component', () => {
  let comp: MovementEntryDetailComponent;
  let fixture: ComponentFixture<MovementEntryDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MovementEntryDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ movementEntry: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(MovementEntryDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MovementEntryDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load movementEntry on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.movementEntry).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
