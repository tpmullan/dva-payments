module ApplicationHelper

  # Returns the full title on a per-page basis.
  def full_title(page_title)
    base_title = "DVA Payments"
    if page_title.empty?
      base_title
    else
      "#{base_title} | #{page_title}"
    end
  end

  # Return active if it is the current page
  def active_page(page_title, active_title)
    if page_title == active_title
      "ui-btn ui-btn-active ui-btn-up-c ui-btn-inline"
    else
      "ui-btn ui-btn-up-c ui-btn-inline"
    end
  end
end

